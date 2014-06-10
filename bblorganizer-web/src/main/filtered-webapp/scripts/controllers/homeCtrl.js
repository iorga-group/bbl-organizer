'use strict';

angular.module('bblorganizer').controller('HomeCtrl', function ($scope, $http, $filter, ngTableParams) {
	//TODO implémenter le filtre
	
	//scope definition
	$scope.vote = function(session) {
		$http.post('api/votes/vote', {baggerName: session.baggerName, sessionTitle: session.title}).success(function() {
			session.voted = true;
		});
	};
	
	$scope.unvote = function(session) {
		$http.post('api/votes/unvote', {baggerName: session.baggerName, sessionTitle: session.title}).success(function() {
			session.voted = false;
		});
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
	
	angular.forEach(data.baggers, function(bagger) {
		angular.forEach(bagger.sessions, function(session) {
			session.baggerName = bagger.name;
			session.tags = bagger.tags.join(', ');
			
			// create the search field for "sessionsFilter"
			session.searchableText =
				toSearchable(session.baggerName)+':'+
				toSearchable(session.title)+':'+
				toSearchable(session.summary)+':'+
				toSearchable(session.tags);
			
			sessions.push(session);
			
			sessionsPerKey[bagger.name+':'+session.title] = session;
		});
	});

	$http.get('api/votes').success(function(votes) {
		angular.forEach(votes, function(vote) {
			var session = sessionsPerKey[vote.baggerName+':'+vote.sessionTitle];
			if (session) {
				session.voted = true;
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
			var searchableSessionsFilter = sessionsFilter ? toSearchable(sessionsFilter) : null;
			// filter on sessions with the filter input
			filteredSessions = $filter('filter')(sessions, function(session) {
				return searchableSessionsFilter ? session.searchableText.indexOf(searchableSessionsFilter) > -1 : true;
			});
			$scope.tableParams.total(filteredSessions.length);
			$scope.tableParams.page(1);
			$scope.tableParams.reload();
		} else {
			firstTime = false;
		}
	});
});