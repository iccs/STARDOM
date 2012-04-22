#/bin/bash

DEV_DIR="/Users/fotis/Development/idea/"
EVENTS_DIR="$DEV_DIR/eu.alert-project.iccs.events/eu.alert-project.iccs.events.core"
REC_DIR="$DEV_DIR/Recommender/eu.alert-project.iccs.recommender.core"
STARDOM_DIR="$DEV_DIR/STARDOM/eu.alert-project.iccs.stardom.core"

LAUS_PORT="22"
LAUS_KEY="/Users/fotis/.ssh/id_rsa_2048"
LAUS_HOST="laus.perimeter.fzi.de"
LAUS_USER="alerticcs"

pwd

cd $EVENTS_DIR; pwd; mvn clean compile package install || exit 1
cd $REC_DIR; pwd; mvn clean compile package install -Denv=prod -Ddb.port=8889 -Dmaven.test.skip=true || exit 1
cd $STARDOM_DIR; pwd; mvn clean compile package install -Denv=prod -Ddb.port=8889 -DfilterDate=1970-01-01 -Dmaven.test.skip=true || exit 1


#upload
echo "Uploading...."

ssh -p$LAUS_PORT -i "$LAUS_KEY" $LAUS_USER@$LAUS_HOST "mkdir -p ICCS/dist/$(date +"%Y-%m-%d")/wars"
scp -P$LAUS_PORT -i "$LAUS_KEY" $STARDOM_DIR/eu.alert-project.iccs.stardom.alert-connector/target/alert-connector-0.0.2-SNAPSHOT.war $LAUS_USER@$LAUS_HOST:"ICCS/dist/$(date +"%Y-%m-%d")/wars" || exit 1;
scp -P$LAUS_PORT -i "$LAUS_KEY" $STARDOM_DIR/eu.alert-project.iccs.stardom.ui/target/ui.war $LAUS_USER@$LAUS_HOST:"ICCS/dist/$(date +"%Y-%m-%d")/wars"  || exit 1;
scp -P$LAUS_PORT -i "$LAUS_KEY" $REC_DIR/eu.alert-project.iccs.recommender.connector/target/connector-0.0.1-SNAPSHOT.war $LAUS_USER@$LAUS_HOST:"ICCS/dist/$(date +"%Y-%m-%d")/wars" || exit 1;
scp -P$LAUS_PORT -i "$LAUS_KEY" $REC_DIR/eu.alert-project.iccs.recommender.ui/target/ui-0.0.1-SNAPSHOT.war $LAUS_USER@$LAUS_HOST:"ICCS/dist/$(date +"%Y-%m-%d")/wars" || exit 1;

ssh -p$LAUS_PORT -i "$LAUS_KEY" $LAUS_USER@$LAUS_HOST  "./reset-db.sh"
ssh -p$LAUS_PORT -i "$LAUS_KEY" $LAUS_USER@$LAUS_HOST  "ls ICCS/dist/$(date +"%Y-%m-%d")"
ssh -p$LAUS_PORT -i "$LAUS_KEY" $LAUS_USER@$LAUS_HOST  "./deploy.sh ICCS/dist/$(date +"%Y-%m-%d")"






