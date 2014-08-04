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
	function toSearchable(text) {
		return unorm.nfd(text).replace(combining, '').toLowerCase();
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
						session.searchableText =
							toSearchable(session.baggerName)+':'+
							toSearchable(session.title)+':'+
							toSearchable(session.summary)+':'+
							toSearchable(session.tags);
						
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
			
			//var orderedData = params.sorting() ? $filter('orderBy') (sessions, params.orderBy()) : sessions;
			$defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
		}
	});
	
	var firstTime = true;
	$scope.$watch('sessionsFilter', function(sessionsFilter) {
		if (!firstTime) { // ignore first time because ng-table doesn't like .reload() on first time...
			var searchableSessionsFilters = sessionsFilter ? toSearchable(sessionsFilter).split(/\s+/g) : null;
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