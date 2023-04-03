## Workshop 37 

## Server side

```
DO_STORAGE_KEY=```<your access key>```
DO_STORAGE_SECRETKEY=```<your secretkey>```
DO_STORAGE_ENDPOINT=sgp1.digitaloceanspaces.com
DO_STORAGE_ENDPOINT_REGION=sgp1
mvn spring-boot:run
```

## Client Side
```
ng serve --proxy-config proxy-config.js
```

## Railway deployment

```
ng build
```

copy dist files to the spring boot static folder.

## Dev Workflow 

Server side 

0. Connect mysql and execute schema.sql
1. Add deps to the pom.xml (json, jaxb runtime, mysql, aws s3)
2. Setup props on the appln.properties (mysql, do, redis...)
3. Test server --> spring-boot:run
4. Implement app config -> setup connection to s3 do
5. Manually test upload a file to the s3 bucket , discrd it.
6. Implement S3 service ->provide upload method
7. Implement Post mapping on the file upload ctrl
8. Implement file upload SQL repository service

Client Side (Angular)
1. Setup proxy-config --> ng server to route traffic from 4200 to 8080 (server side invocation)
2. npm i ngx-webcam (third lib to use your computer webcam) **
3. Style the app ( material modules ) inject to the app module
4. Inject all the relevant modules -> reactive form , webcam ...etc 
5. Generate components using cli "ng g c , s etc "
6. Enable PWA
```
ng add @angular/pwa --project <project-name>
```
7. Update routing rules (app-routing.module.ts)
8. Implement the model (result)
9. Incorporate nav and router outlet on your landing page
10. Implement camera service
11. Implement cam component able to capture the image (dataURL)
12. Implement upload component
13. Implement view component
