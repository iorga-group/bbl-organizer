'use strict';

angular.module('bblorganizer', [
		'ngRoute',
		'ngSanitize',
		'ngTable',
		'raaj-message-interceptor',
		'raaj-message-service',
		'raaj-progress-interceptor',
		'mgcrea.ngStrap'
	])
	.config(function (raajProgressInterceptorProvider) {
		raajProgressInterceptorProvider.setDefaultMessage('Chargement en cours...');
	})
	.config(function (raajMessageServiceProvider) {
		raajMessageServiceProvider.setBootstrapVersion('3.x');
	})
;