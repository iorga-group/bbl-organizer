'use strict';

angular.module('bblorganizer', [
		'ngRoute',
		'ngTable',
		'raaj-message-interceptor',
		'raaj-message-service',
		'raaj-progress-interceptor',
	])
	.config(function (irajProgressInterceptorProvider) {
		irajProgressInterceptorProvider.setDefaultMessage('Chargement en cours...');
	})
	.config(function (irajMessageServiceProvider) {
		irajMessageServiceProvider.setBootstrapVersion('3.x');
	})
;