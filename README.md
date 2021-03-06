# ml-gen-fake-xml-data

*fake-xml.xqy* is an XQuery tool to allow the creation of fake XML data for fast testing.

This is an initial (and very early) start on a library that allows the creation of different types of random data (random dateTimes, random strings, elements containing lists of random words)

* The current checked in code contains two word lists: one containing 21877 individual words and one containing 1510 "common" words 
* A function to generate an arbitrary dateTime within a 40-year date range (starting from 1980)
* Generation of V4 UUIDS

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