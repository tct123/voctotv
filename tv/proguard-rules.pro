-dontobfuscate

-keepattributes Signature, Exceptions
-keepclasseswithmembers interface * {
    @retrofit2.http.* <methods>;
}

-keepattributes *Annotation*, InnerClasses
-keep @kotlinx.serialization.Serializable class * {
    <fields>;
    <init>(...);
}
-keepclassmembers class *$$serializer {
    public static const <fields>;
    public <init>(...);
}
