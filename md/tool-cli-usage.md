# Using Open API Diff from the command line

The absolute most simple way to get started, is issuing the following command after building the project:

     java -Djava.ext.dirs=open-api-diff/sample-api:lib 
          -cp target/open-api-diff-0.10.1.jar dk.hoejgaard.openapi.diff.OpenAPIDiff
      
That is equivalent to:

    java -Djava.ext.dirs=open-api-diff/sample-api:lib 
         -cp target/open-api-diff-0.10.1.jar dk.hoejgaard.openapi.diff.OpenAPIDiff 
             ./sample-api/elaborate_example_v1.json 
             ./sample-api/elaborate_example_v3f.json 
             ./target/output/reports 
             APIDiff.txt 
             all full 1
      
That is equivalent to:

    java -Djava.ext.dirs=open-api-diff/sample-api:lib 
         -cp target/open-api-diff-0.10.1.jar dk.hoejgaard.openapi.diff.OpenAPIDiff 
             ./sample-api/elaborate_example_v1.json 
             ./sample-api/elaborate_example_v3f.json 
             ./target/output/reports 
             APIDiff.txt 
             a f 1

The syntax is (with no line breaks):

    java -Djava.ext.dirs=open-api-diff/sample-api:lib 
         -cp target/open-api-diff-0.10.1.jar dk.hoejgaard.openapi.diff.OpenAPIDiff
             [existing API = ./sample-api/elaborate_example_v1.json] 
             [future API = ./sample-api/elaborate_example_v3f.json] 
             [target report folder = ./target/output/reports] 
             [report filename and format = APIDiff.txt]
             [diff depth = a/all] [maturity = f/full] [versions = 1]
      `

If you want a Markdown report (use the .md file extension):

    java -Djava.ext.dirs=open-api-diff/sample-api:lib 
         -cp target/open-api-diff-0.10.1.jar dk.hoejgaard.openapi.diff.OpenAPIDiff 
             ./sample-api/elaborate_example_v1.json 
             ./sample-api/elaborate_example_v3f.json 
             ./target/output/reports 
             APIDiff.md
             a f 1

If you want a HTML report (use the .html file extension):

    java -Djava.ext.dirs=open-api-diff/sample-api:lib 
         -cp target/open-api-diff-0.10.1.jar dk.hoejgaard.openapi.diff.OpenAPIDiff 
             ./sample-api/elaborate_example_v1.json 
             ./sample-api/elaborate_example_v3f.json
             ./target/output/reports
             APIDiff.html
             a f 1
