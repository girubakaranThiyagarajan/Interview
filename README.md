# Interview

This is a java application for managing Claims and Process the claims in batch mode.

This project uses Java 11.

## Local Environment Setup

### The Basics

1. Clone repository
2. Open in your favorite IDE
3. Install Maven packages - `mvn clean package`

## How to Run 
 ### Command Line
  Open your favourite command line tool like CMD or Git Bash and run the below
  `$ mvn spring-boot:run`
  ### Intellij 

Go to DemoApplication.java , Right click and select Run'DemoApplication'

## How to Submit Claim

Note: Postman collection file `Claims.postman_collection.json` is commited to this repo
Call http://localhost:8080/claims/submit

```bash
{
    "policyHolderId": 2,
    "dateOfService": "10-12-2022",
    "categoryId":4,
    "billedAmt": 200
}
```
### Submission Response
```bash
{
    "claimId": 2,
    "status": "Claim Submitted"
}
```
## How to Process Individual Claim
call http://localhost:8080/claims/process?claimId=1
```bash
claimId part of url is the claim to be processed
```
#### Process Response 
```bash
{
"policyId": "POLICY1",
"policyHolderId": 2,
"dateOfService": "2022-10-12T00:00:00.000+00:00",
"category": "Outpatient Services",
"subCategory": "X-RAYS",
"billedAmt": 200.00,
"policyHolderPays": 120.0000,
"planPays": 80.0000,
"ruleUsed": "40.00% AFTER DEDUCTIBLE",
"indAccumulatedDed": 120.0000,
"famAccumulatedDed": 120.0000,
"errorCode": null,
"errorMessage": null,
"processMessage": "ANNUAL DECUCTIBLE  (INDIVIDUAL or FAMILY) not met- plan pays 40.00%"
}
```
### How to Trigger Claim processing Batch Job

POST call http://localhost:8080/batch/processClaims

```bash
{
    "inputfilepath": "C:\\Users\\Giru\\claim.CSV",
    "outputFilePath": "C:\\Users\\Giru\\claimProcessed.CSV"
}
```
Note: Both input and output file path should  be Absolute 
 Calling this job will process all the claims in claim.csv 
, process it, update the claims table as Processed and write the 
process response details to the claimProcessed.csv 
Check the  processed file  claim.csv and claimProcessed.csv for example 