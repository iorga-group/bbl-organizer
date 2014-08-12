'use strict';

angular.module('bblorganizer').controller('HomeCtrl', function ($scope, $http, $filter, ngTableParams, authenticationService) {

	function updateVotes(session) {
		session.voted = session.votes[authenticationService.userPrincipal().name]; // true if I have voted
		var voters = Object.keys(session.votes);
		session.voterNames = Object.keys(session.votes).join(', ');
		session.voterNumber = voters.length;
	}
	
	//scope definition
	$scope.vote = function(session) {
		$http.post('api/votes/vote', {baggerName: session.baggerName, sessionTitle: session.title}).success(function() {
			session.votes[authenticationService.userPrincipal().name] = true;
			updateVotes(session);
		});
	};
	
	$scope.unvote = function(session) {
		$http.post('api/votes/unvote', {baggerName: session.baggerName, sessionTitle: session.title}).success(function() {
			delete session.votes[authenticationService.userPrincipal().name];
			updateVotes(session);
		});
	};
	
	$scope.downloadCSV = function() {
		var csvStr = 'Bagger;Titre;Nb votes;Votants\n',
			sessions = $scope.sessions;
		angular.forEach(sessions, function(session) {
			csvStr += session.baggerName + ';' + session.title + ';' + session.voterNumber + ';' + session.voterNames + '\n';
		});
		var blob = new Blob([csvStr], {type: 'text/csv;charset=utf-8'});
		saveAs(blob, 'bbl_'+new Date().getTime()+'.csv');
	};
	
	//initialization
	
	var sessions = [];
	var sessionsPerKey = {};
	$scope.sessions = sessions;
	var filteredSessions = sessions;

	/*
	var data;
	eval(baggersFile); // root with "var data = {baggers: [...
	var baggers = data.baggers;
	*/

	var combining = /[\u0300-\u036F]/g; // Use XRegExp('\\p{M}', 'g');
	
	function removeAccentThenLowerCase(text) {
		return unorm.nfd(text).replace(combining, '').toLowerCase();
	}
	
	function toSearchable(text) {
		// remove accent then lower case and delete HTML markup
		return removeAccentThenLowerCase(text).replace(/(<[^>]*?>)/g, '');
	}
	
	function toSearchableSameLength(text) {
		// remove accent then lower case
		text = removeAccentThenLowerCase(text);
		// remove HTML markup, replace by space in order to have the same length than the original string
		var re = /(<[^>]*?>)/g;
		var result = null, newText = '', lastMatch = 0;
		while ((result = re.exec(text)) !== null) {
			newText += text.substring(lastMatch, result.index); // add orig text until match
			newText += new Array(re.lastIndex - result.index + 1).join(' '); // replace by spaces, thanks to http://stackoverflow.com/a/14343876/535203
			lastMatch = re.lastIndex;
		}
		newText += text.substring(lastMatch); // add last part
		return newText;
	}
	
	var searchableFields = [
		'baggerName',
		'title',
		'summary',
		'tags'
	];
	
	function initSessionDisplayableData(session) {
		session.displayableData = {};
		angular.forEach(searchableFields, function(searchableField) {
			session.displayableData[searchableField] = session[searchableField];
		});
	}
	
	// init baggers sessions
	angular.forEach(data.baggers, function(bagger) {
		angular.forEach(bagger.cities, function(city) {
			if (city === 'Paris' || city === 'Versailles') { // filtering on city
				angular.forEach(bagger.sessions, function(session) {
					var sessionKey = bagger.name+':'+session.title;
					
					if (!sessionsPerKey[sessionKey]) { // register only the first session found if a single person have multiple authorized cities
						session.baggerName = bagger.name;
						session.tags = bagger.tags.join(', ');
						session.votes = {}; // field : username, value : true
						session.voterNumber = 0;
						session.voterNames = '';
						
						// create the search field for "sessionsFilter"
						var searchableTexts = [];
						angular.forEach(searchableFields, function(searchableField) {
							searchableTexts.push(toSearchable(session[searchableField]));
						});
						session.searchableText = searchableTexts.join(':');
						
						initSessionDisplayableData(session);
						
						session.baggerURL = 'http://www.brownbaglunch.fr/baggers.html#'+bagger.name.replace(/ /g, '_')+'_'+city;
						
						sessions.push(session);
						
						sessionsPerKey[sessionKey] = session;
					}
				});
			}
		});
	});

	$http.get('api/votes/list').success(function(votes) {
		angular.forEach(votes, function(vote) {
			var session = sessionsPerKey[vote.baggerName+':'+vote.sessionTitle];
			if (session) {
				session.votes[vote.userName] = true;
				updateVotes(session);
			}
		});
	});
	
	$http.get('api/sessions/achieved').success(function(achievedSessions) {
		angular.forEach(achievedSessions, function(achievedSession) {
			var session = sessionsPerKey[achievedSession.baggerName+':'+achievedSession.title];
			if (session) {
				session.achieved = true;
			}
		});
	});

	var firstTime = true;
	function toSearchableSessionsFilters(sessionsFilter) {
		return sessionsFilter ? toSearchable(sessionsFilter).split(/\s+/g) : null;
	}
	$scope.tableParams = new ngTableParams({
		page : 1, // show first page
		count : 10, // count per page
		sorting : {
			title : 'asc' // initial sorting
		}
	}, {
		total : filteredSessions.length, // length of data
		getData : function($defer, params) {
			// use build-in angular filter
			var orderedData = params.sorting() ? $filter('orderBy') (filteredSessions, params.orderBy()) : sessions;
			
			var sessionsToDisplay = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());
			
			// let's highlight the content to display with the searchableSessionsFilters if any
			var sessionsFilter = $scope.sessionsFilter;
			if (!firstTime) {
				var searchableSessionsFilters = toSearchableSessionsFilters(sessionsFilter);
				angular.forEach(sessionsToDisplay, function(session) {
					if (session.displayableData.sessionsFilter !== sessionsFilter) {
						// this session highlights has not been already computed for that session filters
						angular.forEach(searchableFields, function(searchableField) {
							var origField = session[searchableField];
							var searchField = toSearchableSameLength(origField);
							// search for any of the filters, flank them by \u0002 & \u0003 in order to later replace them in the orig field by highlight spans
							if (searchableSessionsFilters) {
								searchField = searchField.replace(new RegExp('('+searchableSessionsFilters.join('|')+')', 'g'), '\u0002$1\u0003');
							}
							// now iterate on matching parts in the searchField, replace them in the origField with highlight spans
							var numberOfBoundaries = 0, searchIndex = -1, origIndex = 0, newField = '';
							while ((searchIndex = searchField.indexOf('\u0002', searchIndex + 1)) > -1) {
								newField += origField.substring(origIndex, searchIndex - numberOfBoundaries)+'<span class="highlight">';
								origIndex = searchIndex - numberOfBoundaries;
								numberOfBoundaries++;
								searchIndex = searchField.indexOf('\u0003', searchIndex);
								newField += origField.substring(origIndex, searchIndex - numberOfBoundaries)+'</span>';
								origIndex = searchIndex - numberOfBoundaries;
								numberOfBoundaries++;
							}
							// copy the last part of the origField
							newField += origField.substring(origIndex);
							// and finaly replace the displayable field
							session.displayableData[searchableField] = newField;
						});
						session.displayableData.sessionsFilter = sessionsFilter;
					}
				});
			}
			$defer.resolve(sessionsToDisplay);
		}
	});
	
	$scope.$watch('sessionsFilter', function(sessionsFilter) {
		if (!firstTime) { // ignore first time because ng-table doesn't like .reload() on first time...
			var searchableSessionsFilters = toSearchableSessionsFilters(sessionsFilter);
			// filter on sessions with the filter input
			filteredSessions = $filter('filter')(sessions, function(session) {
				if (searchableSessionsFilters) {
					var matchAll = true;
					for (var i = 0 ; i < searchableSessionsFilters.length ; i++) {
						matchAll = matchAll && session.searchableText.indexOf(searchableSessionsFilters[i]) > -1;
					}
					return matchAll;
				} else {
					return true;
				}
			});
			$scope.tableParams.total(filteredSessions.length);
			$scope.tableParams.page(1);
			$scope.tableParams.reload();
		} else {
			firstTime = false;
		}
	});
	
	$scope.popoverContent = '{{session.voterNames}}';
});