'use strict';

angular.module('bblorganizer').config(function ($routeProvider) {
	$routeProvider
	.when('/', {
		controller: 'HomeCtrl',
		templateUrl: 'templates/views/home.html'
	});
});