pipeline {
  agent {
    node {
      label 'android'
    }

  }
  stages {
    stage('Compile') {
      agent {
        node {
          label 'android'
        }

      }
      steps {
        sh './gradlew compileDebugSources'
      }
    }
    stage('Build APK debug') {
      parallel {
        stage('Build APK') {
          agent {
            node {
              label 'android'
            }

          }
          steps {
            sh './gradlew assembleDebug'
            archiveArtifacts '**/*.apk'
          }
        }
        stage('Build APK release') {
          steps {
            sh './gradlew assembleRelease'
            archiveArtifacts '**/*.apk'
          }
        }
      }
    }
    stage('Deploy') {
      when {
        branch 'beta'
      }
      environment {
        SIGNING_KEYSTORE = credentials('my-app-signing-keystore')
        SIGNING_KEY_PASSWORD = credentials('my-app-signing-password')
      }
      post {
        success {
          mail(to: 'beta-testers@example.com', subject: 'New build available!', body: 'Check it out!')

        }

      }
      steps {
        archiveArtifacts '**/*.apk'
      }
    }
  }
  post {
    failure {
      mail(to: 'android-devs@example.com', subject: 'Oops!', body: "Build ${env.BUILD_NUMBER} failed; ${env.BUILD_URL}")

    }

  }
  options {
    skipStagesAfterUnstable()
  }
}