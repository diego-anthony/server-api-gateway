@Library(['git_essalud_lib','common_essalud_lib','docker_essalud_lib','gradle_essalud_lib']) _

def gitLib = new git_essalud_lib()
def commonLib = new common_essalud_lib()
def dockerLib = new docker_essalud_lib()
def gradleLib = new gradle_essalud_lib()

pipeline {

    agent any

    tools {
        'org.jenkinsci.plugins.docker.commons.tools.DockerTool' 'docker-tool'
        gradle 'gradle-tool'
    }

    environment {
        GROUP_ID = '7b7f02e0-576c-4475-928d-d378bfc3f79c'
        APPLICATION_ID = '7e0055d6-7fa3-49e3-84a3-bf2944e441c9'
    }

    options {
        skipStagesAfterUnstable()
        disableConcurrentBuilds abortPrevious: true
        buildDiscarder(logRotator(numToKeepStr: "${JOB_MAX_DAYS}", daysToKeepStr: "${JOB_MAX_BUILDS}"))
    }

    stages {

        stage('Check Tools') {
            steps {
                script {
                    commonLib.msgJobBuildStarted()
                    gradleLib.showToolVersion()
                    dockerLib.showToolVersion()
                 }
            }
        }

        stage('Clone Repository') {
            steps {
                checkout scm

                script {
                    gitLib.cloneEnv()
                    commonLib.showWsFiles()
                }
            }
        }

        stage('Build Project') {
            steps {
                script {
                    gradleLib.buildProject('-x test')
                    commonLib.showWsFiles()
                }
            }
        }

        stage('Build Image') {
            steps {
                script { dockerLib.buildImage() }
            }
        }

        stage('Push Image') {
            steps {
                script { dockerLib.pushImage() }
            }
        }

        stage('Deploy') {
            steps {
                script { dockerLib.runContainer() }
            }
        }

    }

    post {
        always { cleanWs() }
        success { script { commonLib.msgJobBuildSuccess() } }
        failure { script { commonLib.msgJobBuildFailed() } }
        unstable { script { commonLib.msgJobBuildUnstable() } }
    }

}