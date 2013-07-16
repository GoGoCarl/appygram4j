Appygram4J
==========

Appygram4J is a simple Appygram connector for Java, supporting all 
basic functionality for sending an Appygram to Appygram.com from 
any Java application.

## Getting Started

Once you introduce com.appygram.java into your project dependencies, 
you'll need to call <code>Appygram.configure</code> to set up Appygram. 
The configuration call takes an AppygramConfig object that allows 
you to set the following properties:

*   key (required) - your Appygram API key.
*   topic - the default topic for all Appygrams (default null)
*   url - the URL of the Appygram endpoints (defaults to current)
*   platform - add'l information about your platform
*   software - add'l information about your software
*   allowThreads - set to false to disable sending Appygrams in 
    the background (Threaded).  Defaults to true.

Create this object, set the appropriate information.  For example:

```java
Appygram.configure(new AppygramConfig("my-API-key"));
```

Now you are ready to create Appygrams.

### Sending Appygram Messages

To create an Appygram Message, you can simply call:

```java
AppygramMessage message = Appygram.create();
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
Appygram.send(message);
```

This will send Appygram information to your specified endpoint, 
and you're done.

### Anonymous Exceptions

If you want to quickly handle exceptions anonymously, you can use 
a simple code block like this:

```java
try {
  String fail = null;
  fail.toString();
} catch (NullPointerException e) {
  Appygram.send(e);
}
```

### Listen for Appygrams

If you want to be notified in code when an Appygram has been sent, 
you can create a class that implements <code>AppygramEventHandler</code>. 
This interface has a single function, <code>afterSend</code> that takes 
an AppygramEvent containing the message that was sent, a success boolean, 
and the response message, if available.  To set, call:

```java
Appygram.addAfterSendHandler(handler);
```

### Appygram Topics

To get a listing of all the topic information that you have set up on 
Appygram.com, you can call:

```java
Appygram.topics()
```

This will yield a list of all your topics, which you can present to 
your client.

Appygram <http://www.appygram.com>
