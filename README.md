# Grouper AWS SecretsManger README

>dependencies:
aws-java-sdk-1.11.319.jar
httpcore-4.4.9.jar
jackson-core-2.6.7.jar
aws-java-sdk-core-1.11.319.jar
httpclient-4.5.5.jar
jackson-annotations-2.6.0.jar
jackson-databind-2.6.7.1.jar

These can be found the the `third-party/libs/` folder of the aws-java-sdk-1.11.319 sdk

>usage from gsh:

groovy:000> edu.internet2.middleware.grouperClient.config.SecretsManagerElClass.getSecret('/js-grouper/dev','username')

>usage in grouper.hibernate.properties:

hibernate.connection.url.sm.elConfig            = ${edu.internet2.middleware.grouperClient.config.SecretsManagerElClass.getSecret('/js-grouper/dev','url')}
hibernate.connection.url                        = $$hibernate.connection.url.sm$$

hibernate.connection.username.sm.elConfig       = ${edu.internet2.middleware.grouperClient.config.SecretsManagerElClass.getSecret('/js-grouper/dev','username')}
hibernate.connection.username                   = $$hibernate.connection.username.sm$$

hibernate.connection.password.sm.elConfig       = ${edu.internet2.middleware.grouperClient.config.SecretsManagerElClass.getSecret('/js-grouper/dev','password')}
hibernate.connection.password                   = $$hibernate.connection.password.sm$$
