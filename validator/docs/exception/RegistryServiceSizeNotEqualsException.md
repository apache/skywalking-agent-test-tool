# RegistryServiceSizeNotEqualsException

## Format
RegistryServiceSizeNotEqualsException  SERVICE_NAME<br/>
expected:  EXPECTED_SIZE<br/>
actual:    ACTUAL_SIZE

## Cause
The `RegistryServiceSizeNotEqualsException` caused by the size of the registry service is different between expected and actual.

## Check Points
1. Check if the size of the registry service that you expected
2. Check if the `agent.service_name` is duplicated in the test case project