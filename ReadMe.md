serverless invoke --function getPhoto --log

serverless invoke --function postPhoto --log

curl -i https://yntwwungi9.execute-api.us-east-1.amazonaws.com/dev/api/v1/user/tallaa/photo


curl -vX POST https://yntwwungi9.execute-api.us-east-1.amazonaws.com/dev/api/v1/user/tallaa/photo -d @test.json --header "Content-Type: application/json"





sls logs --function postPhoto




curl -i --request POST -H "Accept: image/png" -H "Content-Type: image/png" --data-binary "@Anil.png" https://jcslooil2b.execute-api.us-east-1.amazonaws.com/dev/api/v1/user/tallaa/photo

# serverless-photo-service
