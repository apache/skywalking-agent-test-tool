# agent-integration-testtool
The agent-integration-testtool project is used that to validate the different of Skywalking Java Agent generate 
data item between you expect and the actual generate.

## Assert Exceptions
The following are the exceptions that may occur in the validate tools.
### Data file
* [IllegalDataFileException](./docs/exception/IllegalDataFileException.md)

### Registry Item
#### Service
* [RegistryInstanceOfServiceNotFoundException](./docs/exception/RegistryInstanceOfServiceNotFoundException.md)
* [RegistryServiceNotFoundException](docs/exception/RegistryServiceNotFoundException.md)
* [RegistryServiceSizeNotEqualsException](docs/exception/RegistryServiceSizeNotEqualsException.md)

#### Instance
* [RegistryInstanceOfServiceNotFoundException](./docs/exception/RegistryInstanceOfServiceNotFoundException.md)
* [RegistryInstanceSizeNotEqualsException](./docs/exception/RegistryInstanceSizeNotEqualsException.md)
* [RegistryInstancesNotEqualsException](./docs/exception/RegistryInstancesNotEqualsException.md)

#### Operation name
* [ActualRegistryOperationNameEmptyException](./docs/exception/ActualRegistryOperationNameEmptyException.md)
* [RegistryOperationNameNotFoundException](./docs/exception/RegistryOperationNameNotFoundException.md)
* [RegistryOperationNamesNotFoundException](./docs/exception/RegistryOperationNamesNotFoundException.md)
* [RegistryOperationNamesOfServiceNotFoundException](./docs/exception/RegistryOperationNamesOfServiceNotFoundException.md)

### Segment
* [ActualSegmentItemEmptyException](./docs/exception/ActualSegmentItemEmptyException.md)
* [SegmentItemNotFoundException](./docs/exception/SegmentItemNotFoundException.md)
* [SegmentNotFoundException](./docs/exception/SegmentNotFoundException.md)

### Segment Ref
* [SegmentRefNotFoundException](./docs/exception/SegmentRefNotFoundException.md)
* [ParentSegmentNotFoundException](./docs/exception/ParentSegmentNotFoundException.md)
* [SegmentRefSizeNotEqualsException](./docs/exception/SegmentRefSizeNotEqualsException.md)
* [SegmentSizeNotEqualsException](./docs/exception/SegmentSizeNotEqualsException.md)
