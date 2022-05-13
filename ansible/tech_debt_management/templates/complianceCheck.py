
# *************************************************************************
# Owner: Rutik Nisarg Samal <rutik.samal@oracle.com>
# Reviewer: Rethna kumar Subramonian Uma <rethna.kumar@oracle.com>
# Script Name: cpmplianceCheck.py
# Description: This script will check the Software version of the host and check whether
#              the version is compliant or not.
# *************************************************************************

import socket
import os

hostname = socket.gethostname()
pwd = os.getcwd()
# fileName = pwd + "/input.csv"

print "Checking the Software Version of host:", hostname
# Check for compliance status of the Software version installed on the host.


def checkOSVersion():
    with open("/etc/os-release") as myfile:
        for line in myfile:
            try:
                key, value = line.split("=")
            except ValueError:
                pass
            if key == "NAME":
                os_name = value
            elif key == "VERSION":
                os_version = value
    os_name = os_name.replace('"', '').strip()
    os_version = os_version.replace('"', '').strip()
    return os_name, os_version


def checkSoftwareVersion(fileName):
    rows = []

    hostname_to_search = hostname
    with open(fileName, newline='') as csvfile:
        for row in csvfile:
            if hostname_to_search in row:
                if 'Non-Compliant' in row:
                    rows.append(row)

    if len(rows) == 0:
        compliance_status = True
    else:
        compliance_status = False

    return compliance_status, rows


def checkOSVersionCompliance(os_name, os_version):
    with open("version.config") as myfile:
        for line in myfile:
            try:
                key, value = line.split("=")
            except ValueError:
                pass
            if key == "Oracle Linux Server":
                compliant_os_version_list = list(value.strip().split(","))
                compliant_os_version_list = [float(i) for i in compliant_os_version_list]
                latest_os_version = compliant_os_version_list[0]
                compliant_os_version_list = [int(i) for i in compliant_os_version_list]
                if key == os_name:
                    if int(float(os_version)) in compliant_os_version_list:
                        return latest_os_version, "Compliant"
                    else:
                        return latest_os_version, "Non-Compliant"


def complianceStatus(compliance_status, rows):

    if compliance_status is True:
        print "This host is compliant, proceeding with deployment.", ","
        return compliance_status
    elif compliance_status is False:
        # print "List of non-compliant Softwares present in the host:"
        # for line in rows:
        #     print line

        return compliance_status
    else:
        print "ERROR: Compliance status is not handles, please check.", ","


os_name, os_version = checkOSVersion()
latest_os_version, os_compliance_status = checkOSVersionCompliance(
    os_name, os_version)

print "hostname:", hostname
print "os_name:", os_name
print "os_version:", os_version
print "latest_os_version:", latest_os_version
print "os_compliance_status:", os_compliance_status
