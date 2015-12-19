# ml-gen-fake-xml-data

XQuery tool to allow the creation of fake XML data for fast testing

## Example

```xquery
element example {
    element simple-id{xdmp:random()},
    element words {local:random-words(50)},
    element guid {local:generate-uuid-v4()},
    element date {local:random-date()},
    element azAZstring {local:random-alpha-string(100)}
}
```

Returns 

```xml
<example>
    <simple-id>12620581215602732299</simple-id>
    <words>adoptive mezzanine decomposition slate pressurized contortion virus finished video game commentary shantytown slide disillusionment jockstrap nail snappy rightful expressive rambling hew commercial bank irresistible queen rip-off peacemaker sportsman belong exile skin cloak-and-dagger ventilator vegetation collaborate vindictive yr. illustrate cremation empty poorly proofread Frisbee what contingent sneaky archipelago phooey guest regularity spaceship in-depth</words>
    <guid>76013e06-ec1a-11be-91d7-5b37b9eaa7f6</guid>
    <date>2003-01-01T19:44:08Z</date>
    <azAZstring>EuxkObPWVegWDhCKIvpCjkEwUCSPXbVouqJVfejDhQHsRPmlGQRPHjTHsejdrNAlvkkVwdPCjEAJHtdxoMrGVNOFoZrCIFYcUZqv</azAZstring>
</example>
```