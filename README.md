# JTestDoc
JTestDoc is an ant task that checks your test documentation and generates errors or warnings depending on how you configure it.

# Set Up

To add JTestDoc to your project, you can add the jar to your build path or if you are using maven you can just add the dependency :
```xml
<dependency>
  <groupId>JTestDoc</groupId>
  <artifactId>JTestDoc</artifactId>
  <version>1.0</version>
  <classifier>jar-with-dependencies</classifier>
</dependency>
```

Now add the following to your ant build task. Note that this process all subfolders.

```xml
<target name="main">
	<checkdoc destination="documentation.html" blocking="false">
			<fileset dir="src/tests/" includes="*.java"></fileset>
	</checkdoc>
</target>
	
<taskdef name="checkdoc" classname="org.dhatim.jtestdoc.tasks.JDocProcess" classpath="yourpath\JTestDoc-1.0-jar-with-dependencies.jar"/>
```


Changing 'blocking' value to true will result in throwing BuildException instead of Warnings while building your app, and of destination will change the location of the documentation to be generated.

# Required documentation

Each test must have at least the annotation @Test, with the description parameter if you use testng. If you don’t, your test's description will be the content of the method’s javadoc.

Describe the initial state in comments before writing any instructions in your methods. For both the test’s description and the initial state, you can use the markdown syntax to shape your html.

Write “Step:“ to create a new step.

Always write your expected result on the line right before your assert calls, and write it for each one. You can also add an expected result in any comment, but if it is not right before an assert call you’ll have to add the prefix “Expected result:” to your comment.
