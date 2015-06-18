# JTestDoc
JTestDoc is an ant task that checks your test documentation and generates error or warning depending on how you set it.

# Set Up

To add JTestDoc to your project, add the jar to your path and the following to your ant build task. Note that this will open all subfolders.

<taskdef name="checkdoc" classname="org.dhatim.jtestdoc.tasks.JDocProcess">
		 <classpath>
	 	  <pathelement path="yourpath\JTestDoc-0.0.1.jar"/>
	      <pathelement path="yourpath\javaparser-core-2.1.0.jar"/>
	      <pathelement path="yourpath\guava-18.0.jar"/>
	      <pathelement path="yourpath\gson-2.3.1.jar"/>
	    </classpath>
</taskdef>	

<target name="main">
	<checkdoc destination="documentation.html" blocking="false">
			<fileset dir="src/test/">
					    <include name="**/*.java" />
			</fileset>
	</checkdoc>
</target>


Changing to blocking value to true will result in throwing BuildException instead of Warnings while building your app, and of destination will change the destination of the documentation to be generated.

# Required documentation

Each test must have at least the annotation @Test, with the parameter description if you use testng. If you don’t, your test description will be the content of the method’s javadoc.

Describe the initial state in comments before writing any instructions in your methods. For both the test’s description and the initial state, you can use the markdown syntax to shape your html.

Write “Step:“ to create a new step.

Always write your expected result on the line right before your assert calls, and write it for each one. You can also add an expected result in any comment, but if it is not right before an assert call you’ll have to add the prefix “Expected result:” to your comment.
