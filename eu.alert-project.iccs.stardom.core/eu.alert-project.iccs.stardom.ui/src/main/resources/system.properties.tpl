jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/alert_dev
jdbc.username=alert
jdbc.password=1234

##########################################################
#                Authenticaation
##########################################################

auth.loginUrl=http://imu.ntua.gr
auth.sessionTimeout=36000
auth.adminEmail=foo@acme.lan

##########################################################
#               ActiveMQ
##########################################################

activemq.clientId=STARDOMUI
#activemq.url=tcp://93.87.17.115:61616
activemq.url=tcp://localhost:61616
#activemq.url=tcp://laus.perimeter.fzi.de:61616
#activemq.url=tcp://www.cimcollege.rs:61616
activemq.userName=
activemq.password=
activemq.cacheLevel=3
activemq.recoveryInterval=60000
activemq.processDisabled=false


##########################################################
#               UI
##########################################################


log.file=/tmp/run.log
#ui
ui.page.size=5
ui.paginator.size=4


##########################################################
#               Identifier Configuration
##########################################################

#This value defines what the Identification Factor must be over, to consider 2 profiles a match
identifier.threshold=0.74


# The weight given to a value to be a single one accross the information sources
identifier.weights.univocity=0.60


# The weight given to a single property containing many values
identifier.weights.valuesperuser=0.35

# The weight given to a property may have a misleading level
identifier.weights.misleading=0.20


##########  Properties ###########
#
# Each property has the following levels
#
# ul - Univocity Level
# vpl - Values Per User
# ml - Misleading Level
#
##################################

#### FIRSTNAME - Property
identifier.properties.firstname.ul=0.8
identifier.properties.firstname.vpl=0.8
identifier.properties.firstname.ml=0.2

#### LASTNAME - Property
identifier.properties.lastname.ul=0.8
identifier.properties.lastname.vpl=0.8
identifier.properties.lastname.ml=0.8

#### USERNAME - Property
identifier.properties.username.ul=0.8
identifier.properties.username.vpl=0.2
identifier.properties.username.ml=0.2


#### EMAIL - Property
identifier.properties.email.ul=1
identifier.properties.email.vpl=0.6
identifier.properties.email.ml=0.4


# Make a copy of this file to set up your mail sender
# Sending e-mails
mail.host=smtp.gmail.com
mail.username=iccs.stardom@gmail.com
mail.password=E:8^4dQD474P
mail.port=465
mail.protocol=smtps
