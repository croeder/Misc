#!/usr/bin/python


from ruleState import RuleState
from early import Early
import sys


r = RuleState.parse_RuleState("x-->test,x")
r.Test()
