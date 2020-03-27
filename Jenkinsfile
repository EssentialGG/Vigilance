pipeline {
  agent any
  stages {
    stage('Initialize') {
      steps {
        sh '''echo username=sk1erdeploy'\n'password=${github-deploy} > gradle.properties.private'''
        sh "./gradlew preprocessResources"
      }
    }
    stage('Build') {
      steps {
        sh "./gradlew install -PBUILD_ID=${env.BUILD_ID} --no-daemon"
      }
    }


    stage('Report') {
      steps {
        archiveArtifacts 'versions/1.8.9/build/libs/*.jar'
        archiveArtifacts 'versions/1.12.2/build/libs/*.jar'
//         archiveArtifacts 'versions/1.15.2/build/libs/*.jar'
      }
    }
    stage('Deploy') {
        steps {
            //1.8.9
            nexusPublisher nexusInstanceId: 'sk1errepo', nexusRepositoryId: 'maven-releases', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'versions/1.8.9/build/libs/Vigilance1.8.9-${BUILD_ID}.jar'],[classifier: '', extension: '', filePath: 'versions/1.8.9/build/poms/pom-default.xml']], mavenCoordinate: [artifactId: 'Vigilance', groupId: 'club.sk1er', packaging: 'jar', version: '${BUILD_ID}-10809']]]
            //1.12.2
            nexusPublisher nexusInstanceId: 'sk1errepo', nexusRepositoryId: 'maven-releases', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'versions/1.12.2/build/libs/Vigilance1.12.2-${BUILD_ID}.jar'],[classifier: '', extension: '', filePath: 'versions/1.12.2/build/poms/pom-default.xml']], mavenCoordinate: [artifactId: 'Vigilance', groupId: 'club.sk1er', packaging: 'jar', version: '${BUILD_ID}-11202']]]
//          nexusPublisher nexusInstanceId: 'sk1errepo', nexusRepositoryId: 'maven-releases', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'versions/1.15.2/build/libs/Vigilance1.15.2-${BUILD_ID}.jar']], mavenCoordinate: [artifactId: 'Vigilance', groupId: 'club.sk1er', packaging: 'jar', version: '${BUILD_ID}-11502']]]
        }
            }
      }
}
