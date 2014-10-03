JCROMfx
=======

JavaFX persistence made easy - [JCROM](https://code.google.com/p/jcrom/) fork with JavaFX properties support.

About
-----

JCROMfx is a [JCROM](https://code.google.com/p/jcrom/) fork that enable the support of [JavaFX properties](http://docs.oracle.com/javafx/2/binding/jfxpub-binding.htm). 

JCROM is a lightweight framework for mapping Java objects to/from a Java Content Repository (JCR).
 
With JCROMfx you can map your beans with Java and/or JavaFX properties directly to a JCR.
 
How to use it?
--------------

Just follow the JCROM [user guide](https://code.google.com/p/jcrom/wiki/UserGuide). The only difference is that you can use your annotations directly on JavaFX properties. 
 
 
Features
--------
 - Support all JavaFX properties including ListProperty and MapProperty
 - Access the properties through the property getter if available or directly using reflection (this allow to use a lazy creation design pattern)
