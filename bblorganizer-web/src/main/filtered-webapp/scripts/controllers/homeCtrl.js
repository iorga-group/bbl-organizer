'use strict';

angular.module('bblorganizer').controller('HomeCtrl', function ($scope, $http, $filter, ngTableParams) {
	//TODO implémenter le filtre
	
	//scope definition
	$scope.vote = function(session) {
		$http.post('api/votes/vote', {baggerName: session.bagger.name, sessionTitle: session.title}).success(function() {
			session.voted = true;
		});
	};
	
	$scope.unvote = function(session) {
		$http.post('api/votes/unvote', {baggerName: session.bagger.name, sessionTitle: session.title}).success(function() {
			session.voted = false;
		});
	};
	
	//initialization
	
	var sessions = [];
	var sessionsPerKey = {};
	$scope.sessions = sessions;

	/*
	var data;
	eval(baggersFile); // root with "var data = {baggers: [...
	var baggers = data.baggers;
	*/
	
	angular.forEach(data.baggers, function(bagger) {
		angular.forEach(bagger.sessions, function(session) {
			session.bagger = bagger;
			session.tags = bagger.tags.join(', ');
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
		total : sessions.length, // length of data
		getData : function($defer, params) {
			// use build-in angular filter
			var orderedData = params.sorting() ? $filter('orderBy') (sessions, params.orderBy()) : sessions;
			$defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
		}
	});
});