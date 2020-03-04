# RegistryOperationNamesOfServiceNotFoundException

## Format
RegistryOperationNamesOfServiceNotFoundException<br/>
expected: [ OPERATION_NAME_A, OPERATION_NAME_B, ...]<br/>
actual: NOT FOUND

## Cause
The `RegistryOperationNamesOfServiceNotFoundException`  caused by the registry operation name of someone service does not exist in the actual data file.

## Check Points
1. Check if the execute time of entry service of test case more than 40 seconds, Please make it less than 40 seconds.