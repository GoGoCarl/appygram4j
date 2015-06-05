Appygram4J (1.0.2)
==================

Appygram4J is a simple Appygram connector for Java, supporting all 
basic functionality for sending an Appygram to Appygram.com from 
any Java application.

## Download

Download the latest release from the Releases page to get started, or 
add Appygram4J as a dependency using one of the methods below:

[![Release](https://img.shields.io/github/release/GoGoCarl/appygram4j.svg?label=maven)](https://jitpack.io/#GoGoCarl/appygram4j)

### Using Gradle 

```gradle
repositories { 
  maven { url "https://jitpack.io" }
}
dependencies {
  compile 'com.github.GoGoCarl:appygram4j:1.0.2'
}
```

### Using Gradle with Android Studio

If your Android Studio project has a project-level build.gradle with a 
section for <code>allprojects</code>, instead of adding the repositories 
section to the module-level gradle file, instead append the 
<code>maven { url "https://jitpack.io" }</code> line above under the 
<code>allprojects</code> repositories.

```gradle
allprojects {
  repositories {
    jcenter()
    maven { url "https://jitpack.io" }
  }
}
```

### Using Maven

```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>

<dependency>
  <groupId>om.github.GoGoCarl</groupId>
  <artifactId>appygram4j</artifactId>
  <version>1.0.2</version>
</dependency>
 ```

## Getting Started

One you introduce com.appygram.java into your project dependencies, 
the key component to sending Appygrams using Appygram4J is to create 
an instance of <code>AppygramMessenger</code>.  This object will 
allow you to send Appygram messages and traces, as well as retrieve 
a list of topics.

AppygramMessenger is bound to a given API key, so you'll need to 
configure your messenger with properties via <code>AppygramConfig</code>. 
You can do this one of two ways:

### Using a Global AppygramMessenger

If your application will only use one API key for the entire application, 
you can quickly configure a static, global messenger instance by calling 
<code>Appygram.configure</code>. The configuration call takes an 
AppygramConfig object, described below.  From there, you can access the 
AppygramMessenger object from anywhere in your code simply by calling 
<code>Appygram.Global</code>.

### Create specific AppygramMessage instance(s).

If your application may have muliple API keys, or you prefer not to use 
static objects, you can create a specific instance of AppygramMessenger 
by calling <code>Appygram.instance</code>.  This call takes an AppygramConfig 
object, described below, and returns the AppygramMessenger.  Use this 
object to send messages in your application.


Whichever method you choose to setup your AppygramMessenger, you'll need 
to pass in AppygramConfig, which allows you to set the following properties:

*   key (required) - your Appygram API key.
*   topic - the default topic for all Appygrams (default null)
*   url - the URL of the Appygram endpoints (defaults to current)
*   platform - add'l information about your platform
*   software - add'l information about your software
*   allowThreads - set to false to disable sending Appygrams in 
    the background (Threaded).  Defaults to true.
*   logToConsole - true to log errors to stdout, false otherwise 
    (defaults false)

AppygramConfig can also take a Properties object in the constructor. 
The following properties are supported, and correspond to the aforementioned 
properties above, in order:

*   com.appygram.java.key (required)
*   com.appygram.java.topic
*   com.appygram.java.url
*   com.appygram.java.platform
*   com.appygram.java.software
*   com.appygram.java.console - "true" to print to console.
*   com.appygram.java.thread - "true" to allow threads.

Create this object, set the appropriate information.  Then, to configure a 
Global AppygramMessenger:

```java
Appygram.configure(new AppygramConfig("my-API-key"));
```

Or, to create an instance of AppygramMessenger:

```java
AppygramMessenger messenger = Appygram.instance(new AppygramConfig("my-API-key"));
```

Now you are ready to create Appygrams.  For simplicity, the examples 
below will assume you are using the Global AppygramMessenger, but the 
same methods will work if you are using a specific instance as well.

### Sending Appygram Messages

To create an AppygramMessage object, you can simply call:

```java
AppygramMessage message = Appygram.Global.create();
```

This will generate a new message, pre-filled with any defaults you 
specified in your configuration earlier.  From there, you can set 
the following fields:

*   topic - of principal importance in message routing
*   subject
*   message
*   name
*   email
*   phone
*   platform
*   software
*   app_json - Any object assigned to this field will be serialized 
    into JSON.  You can supply a Hash of <String, Object> that will 
    allow you to address objects later by key.  It is null by default.

The AppygramMessage object can be extended to allow you to provide 
your own custom implementation for ease of development.

Once you have your message set, simply call:

```java
Appygram.Global.send(message);
```

This will send Appygram information to your specified endpoint, 
and you're done.

### Handling Exceptions using Traces

Appygram is also equipped to specifically handle exceptions via Traces. 
This is additional information supplied in an AppygramMessage that adds 
functionality to your Appygram. Such improvements include de-duplication 
of similar errors and alerts of how many of some type of error were sent.

Using Traces works almost exactly like normal AppygramMessages.  You 
can supply the same information.  The only differences are that you must 
supply a trace (exception), and the message field is optional, as one 
will be generated for you.  If you do supply a message, the generated 
message will be appended to yours.  Here's an example:

```java
try {
  String fail = null;
  fail.toString();
} catch (NullPointerException e) {
  AppygramTrace trace = Appygram.Global.createTrace(e);
  trace.setName(getUserName());
  trace.setEmail(getUserEmail());
  trace.setSummary("Error Occurred: " + e.getMessage());
  Appygram.Global.trace(trace);
}
```

### Anonymous Exceptions

If you want to quickly handle exceptions anonymously, you can use 
a simple code block like this:

```java
try {
  String fail = null;
  fail.toString();
} catch (NullPointerException e) {
  Appygram.Global.trace(e);
}
```

This will supply a message based on the exception stacktrace and a 
summary based on the exception message.

### Listen for Appygrams

If you want to be notified in code when an Appygram has been sent, 
you can create a class that implements <code>AppygramEventHandler</code>. 
This interface has a single function, <code>afterSend</code> that takes 
an AppygramEvent containing the message that was sent, a success boolean, 
and the response message, if available.  To set, call:

```java
Appygram.Global.addAfterSendHandler(handler);
```

### Appygram Topics

To get a listing of all the topic information that you have set up on 
Appygram.com, you can call:

```java
List<AppygramTopic> topics = Appygram.Global.topics();
```

This will yield a list of all your topics, which you can present to 
your client using the id and name variables on AppygramTopic.

### Logging

All logging is routed through <code>AppygramLogger</code>.  It 
defaults to using the parent handler, and will not log to standard 
out unless you tell it to via AppygramConfig.

Appygram <http://www.appygram.com>
