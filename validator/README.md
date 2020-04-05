# agent-integration-test tool
The agent-integration-test tool project is used that to validate the different of Skywalking Java Agent generate 
data item between you expect and the actual generate.

## Assert Exceptions
The following are the exceptions that may occur in the validate tools.
### Data file
* [IllegalDataFileException](./docs/exception/IllegalDataFileException.md)

### Segment
* [ActualSegmentItemEmptyException](./docs/exception/ActualSegmentItemEmptyException.md)
* [SegmentItemNotFoundException](./docs/exception/SegmentItemNotFoundException.md)
* [SegmentNotFoundException](./docs/exception/SegmentNotFoundException.md)

### Segment Ref
* [SegmentRefNotFoundException](./docs/exception/SegmentRefNotFoundException.md)
* [ParentSegmentNotFoundException](./docs/exception/ParentSegmentNotFoundException.md)
* [SegmentRefSizeNotEqualsException](./docs/exception/SegmentRefSizeNotEqualsException.md)
* [SegmentSizeNotEqualsException](./docs/exception/SegmentSizeNotEqualsException.md)
