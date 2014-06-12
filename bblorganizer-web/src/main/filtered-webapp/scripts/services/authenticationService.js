'use strict';

angular.module('bblorganizer')
	.factory('authenticationService', function ($http) {
		var userPrincipal = null;
		return {
			init: function() {
				$http.get('api/authentication/userPrincipal').success(function(data) {
					userPrincipal = data;
				});
			},
			userPrincipal: function() {
				return userPrincipal;
			}
		};
	})
	.run(function(authenticationService) {
		authenticationService.init();
	})
;