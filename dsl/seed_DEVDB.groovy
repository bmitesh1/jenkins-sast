pipelineJob(JobName) {
	description()
	keepDependencies(false)
    environmentVariables {
        env("environment_type", 'DEV')
        env("DBApplication_Repo_Url", DBAppRepoUrl)
        env("DBApplication_Repo_Branch", DBAppRepoBranch)
        env("orderfilename", order_file_name)
		groovy()
		loadFilesFromMaster(false)
		keepSystemVariables(true)
		keepBuildVariables(true)
		overrideBuildParameters(false)
	}
	definition {
		cpsScm {
                          scm {
                    git {
                        remote {
                            url('https://orahub.oci.oraclecorp.com/oit-entapps-devops/jenkins-pipeline.git')
                            credentials('e0a06baa-a197-4f27-9210-c4a28d35511a')
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
          scriptPath('DevDBJenkinsfile/Jenkinsfile')
	}
	disabled(false)
}
}
