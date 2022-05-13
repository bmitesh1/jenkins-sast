# *************************************************************************
# Owner: Rutik Nisarg Samal <rutik.samal@oracle.com>
# Reviewer: Rethna kumar Subramonian Uma <rethna.kumar@oracle.com>
# Script Name: manageJiraStory.py
# Description: This script will creare Jira Story.
# *************************************************************************


import os
import configparser
import sys
import yaml

from string import Template
from jira import JIRA
from datetime import date
from datetime import timedelta

if len(sys.argv) != 7:
    print("Please provide 'OS Name', 'Remote Host Name', 'Current OS version', 'Latest OS Version' 'Jira Server Password' 'Deployment Environment' as command line argument")
    print("Ex - python3 manageJiraStory.py 'Oracle Linux Server' 'wd0269' 7.9 8.4 password DEV")
    sys.exit(1)

os_name = sys.argv[1]
hostname = sys.argv[2]
current_os_version = sys.argv[3]
latest_os_version = sys.argv[4]
jira_server_password = sys.argv[5]
deployed_environment = sys.argv[6]

config = configparser.ConfigParser(interpolation=None)
config.read('jira.config')
proxy_list = config.get('PROXY', 'PROXY_SETTINGS').split(',')

for proxy in proxy_list:
    os.environ.pop(proxy, None)


with open('app-details.yml') as file:
    # The FullLoader parameter handles the conversion from YAML
    # scalar values to Python the dictionary format
    app_details = yaml.load(file, Loader=yaml.FullLoader)

try:
    jira_project_key = app_details[deployed_environment]['jira_story_project_key']
    print("Jira Project Key:", jira_project_key)
except KeyError:
    print("Please provide a valid deployment environment name or Jira Project Key same as app-details.yml")
    exit(1)

if jira_project_key is None:
    print("Jira Project key is not provided in app-details.yml, please provide the Jira project key.")
    exit(1)

jql_search_str = eval(config.get('JIRA', 'jql_search_str'))
jira_update_comment = config.get('JIRA', 'jira_update_comment')
# print(jql_search_str)

jira_story_description_template = Template(config.get('JIRA', 'jira_story_description_template'))

jira_story_description = jira_story_description_template.safe_substitute(
    hostname=hostname, os_name=os_name, current_os_version=current_os_version, latest_os_version=latest_os_version).replace('\\n', '\n')

issue_dict = eval(config.get('JIRA', 'issue_dict'))
# print(issue_dict)

jira_server_url = config.get('JIRA', 'jira_server_url')
jira_server_user = config.get('JIRA', 'jira_server_user')
# jira_server_password = config.get('JIRA', 'jira_server_password')

jiraOptions = {'server': jira_server_url}
jira = JIRA(options=jiraOptions, basic_auth=(jira_server_user, jira_server_password))


def createJiraStory():
    print("Creating Jira Story")
    jira_story_id = jira.create_issue(fields=issue_dict)
    print(jira_story_id)
    return jira_story_id


def updateJiraStory(jira_issue_list, jira_update_comment):
    print("Updating Jira SD...")
    for issue in jira_issue_list:
        jira.add_comment(issue.key, jira_update_comment)


def checkJiraStoryifexists(jql_search_str):
    # print("Searching for Jira:", jql_search_str)
    jira_issue_list = jira.search_issues(jql_str=jql_search_str)
    return jira_issue_list


jira_issue_list = checkJiraStoryifexists(jql_search_str)
print('jira_issue_list:', jira_issue_list)

if len(jira_issue_list) == 0:
    jira_story_id = createJiraStory()
    print('jira_story_id:', jira_story_id)
else:
    updateJiraStory(jira_issue_list, jira_update_comment)
    print('jira_story_id:', jira_issue_list)
