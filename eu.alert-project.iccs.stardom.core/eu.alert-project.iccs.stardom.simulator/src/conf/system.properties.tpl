##########################################################
#               Database
##########################################################

jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:8889/cvsanaly_alert
jdbc.username=alert
jdbc.password=1234


##########################################################
#               ActiveMQ
##########################################################

activemq.clientId=ICCS-ALERT-SIMULATOR
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
#               Data
##########################################################

data.basePath=

##########################################################
#               KDE
##########################################################
kde.path.commits=KDE/KDE_ALERT.Metadata.CommitNew.Updated.tar.gz
kde.path.issuesNew=KDE/KDE_ALERT.Metadata.IssueNew.Updated.rar
kde.path.issuesUpdate=KDE/KDE_ALERT.Metadata.IssueUpdate.Updated.rar
kde.path.mailnew=KDE/KDE_ALERT.Metadata.MailNew.Updated.rar
kde.path.forums=KDE/KDE_ALERT.Metadata.ForumPostNew.Updated.tar.gz



##########################################################
#               Optimis
##########################################################
optimis.path.commits=Optimis/Optimis_ALERT.Metadata.CommitNew.Updated.rar
optimis.path.issuesNew=Optimis/Optimis_ALERT.Metadata.IssueNew.Updated.rar
optimis.path.issuesUpdate=Optimis/Optimis_ALERT.Metadata.IssueUpdate.Updated.rar
optimis.path.mailnew=Optimis/Optimis_ALERT.Metadata.MailNew.Updated.rar
optimis.path.forums=


##########################################################
#               Petals
##########################################################
petals.path.commits=Petals/Petals_ALERT.Metadata.CommitNew.Updated.rar
petals.path.issuesNew=Petals/Petals_ALERT.Metadata.IssueNew.Updated.rar
petals.path.issuesUpdate=Petals/Petals_ALERT.Metadata.IssueUpdate.Updated.rar
petals.path.mailnew=Petals/Petals_ALERT.Metadata.MailNew.Updated.rar
petals.path.forums=


