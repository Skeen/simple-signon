#!/usr/bin/env python
import os, sys, subprocess

def system(name):
    '''
    Returns true if name==any, or if name matches the system platform.
    '''
    if name == "any":
        return True
    if sys.platform.startswith("linux") and name.startswith("linux"):
        if name ==  "linux64":
            return platform.machine() == "x86_64"
        elif name == "linux32":
            return platform.machine() != "x86_64"
        else:
            return True
    else:
        return sys.platform.startswith(name)

def cores():
    '''
    Returns the number of CPUs in the system
    Source: http://pyprocessing.berlios.de/
    '''
    try:
        count = 0
        if system("linux"):
            count = os.sysconf('SC_NPROCESSORS_ONLN')
        elif system("darwin"):
            count = int(os.popen('sysctl -n hw.ncpu').read())
        elif system("win") or system("cygwin"):
            count = int(os.environ['NUMBER_OF_PROCESSORS'])
        return max(count, 1)
    # if an error occurs simply return one
    except Exception: pass
    return 1

def execute(cmd):
    '''
    Execute system command
    '''
    proc = subprocess.Popen(cmd, shell=True)
    proc.wait()

scons_executeable = "tools/scons/bin/scons"

# Remove the first arg (./make.py)
passed_arguments = sys.argv[1:]
# Make a string of the arguments list
passed_arguments_string = " ".join(passed_arguments)
# Run scons without building anything, in order to generate a dependency tree
# execute(scons_executeable + " --tree=prune,derived -Q --dry-run -j %d %s > depends.log" % (cores(), passed_arguments_string))
# Execute scons with our arguments
# We cannot run this multicore, due to issues with scons
execute(scons_executeable + " -j %d %s" % (cores(), passed_arguments_string))

