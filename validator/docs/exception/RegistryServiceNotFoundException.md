# RegistryServiceNotFoundException

## Format 
RegistryServiceNotFoundException<br/>
expected: EXPECTED_SERVICE<br/>
actual:   NOT FOUND

## Cause by
The `RegistryServiceNotFoundException` caused by one of service code that you write in the expected data file 
cannot found in the actual data file.


## Check points
1. Check the service code is the value of  `agent.service_name` that you configured.

e.g.,
the service that you write in the expected data file:  
```
registryItems:   {
  "test_servce_name":  {
  }
}
```
the service of  `agent.service_name` that you configured: 
`-Dskywalking.agent.service_name=another_service_name`

2. Check the agent of someone project in the test case if it works.