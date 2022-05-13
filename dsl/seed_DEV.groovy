pipelineJob(JobName) {
	description()
	keepDependencies(false)
    environmentVariables {
		env("Build_Type", BuildType)
        env("Application_Repo_Url", AppRepoUrl)
        env("Application_Repo_Branch", AppRepoBranch)
        env("environment_type", 'DEV')
        env("SonarAnalysis", Sonarqube_Analysis)
        env("FunctionalTesting", Functional_Testing)
        env("SAST", SAST_Implementation)
        env("UnitTestCoverage", UnitTest_Coverage)
        env("CoverageValue", Coverage_Value)
        env("enable_webhook_pollscm", PollingMechanism)
		groovy()
		loadFilesFromMaster(false)
		keepSystemVariables(true)
		keepBuildVariables(true)
		overrideBuildParameters(false)
	}
    triggers {
        if (PollingMechanism == 'Webhook')
        {
        gitlabPush {
            buildOnMergeRequestEvents(true)
            buildOnPushEvents(true)
            setBuildDescription(true)
            rebuildOpenMergeRequest('never')
            skipWorkInProgressMergeRequest(true)
        }
        }
        if (PollingMechanism == 'PollSCM')
        {
        scm('H/15 * * * *')
        }
    }


	definition {
		cpsScm {
                          scm {
                    git {
                        remote {
                            url('https://orahub.oci.oraclecorp.com/oit-entapps-devops/jenkins-pipeline.git')
                            credentials('12b63efe-bcf7-44b3-a11f-a6c3c4faa23e')
                        }
                        branch('master')
                        extensions{
                            cleanAfterCheckout()
                            cloneOptions {
                                noTags(true)
                                honorRefspec(true)
                                shallow(true)
                                depth(1)
                            }
                        }
                    }
		}
          scriptPath('DevJenkinsfile/Jenkinsfile')
	}
	disabled(false)
}
}
