## Requirements

#### Application
- Java 11
- Maven

#### Database
- PostgreSQL

## Start locally
```
mvn clean install
java <env variables: -DvariableName='value'> -jar <jar location>
```
## Start locally for development
```
mvn compile
mvn <env variables: -DvariableName='value'> spring-boot:run
```

## Environment variables:

- DB_URL (_default_: 'jdbc:postgresql://localhost:5432/pnu_feedback') - _db url_
- DB_USERNAME (_default_: 'postgres') - _db username_
- DB_PASSWORD (_default_: 'root') - _db password_
- ADMIN_USERNAME (_default_: 'admin') - _admin panel username_
- ADMIN_PASSWORD (_default_: 'admin') - _admin panel password_
- JWT_SECRET (_default_: 'secret') - _jwt secret, that is used to sign tokens for feedback submissions_
- APP_BASE_URL (_default_: 'http://localhost:8080') - _application base url_
- ADMIN_PANEL_URL (_default_: 'admin') - _path to admin panel_
- WEB_ASSETS_LOCATION (_default_: '') - _an absolute path to local libs. If the value is present, js libs will be downloaded from the specified location._

## Assets used by Web UI
<table style="width:100%">
  <tr>
    <th>LIB</th>
    <th>CDN LIB</th>
    <th>SOURCE</th>
    <th>NOTES</th>
  </tr>
  <tr>
    <td>bootstrap.min.css</td>
    <td>https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css</td>
    <td>https://getbootstrap.com/docs/4.4/getting-started/download/</td>
    <td>Use only /bootstrap-4.4.1 2/dist/css/bootstrap.min.css</td>
  </tr>
  <tr>
    <td>font-awesome.min.css</td>
    <td>https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css</td>
    <td>https://fontawesome.com/v4.7/get-started/</td>
    <td>Use only /fonts, /css folders</td>
  </tr>
  <tr>
    <td>jquery-3.5.1.min.js</td>
    <td>https://code.jquery.com/jquery-3.5.1.min.js</td>
    <td>https://code.jquery.com/jquery-3.5.1.min.js</td>
    <td>Download this file</td>
  </tr>
  <tr>
    <td>popper.min.js</td>
    <td>https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js</td>
    <td>https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js</td>
    <td>Download this file</td>
  </tr>
  <tr>
    <td>bootstrap.bundle.min.js</td>
    <td>https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.bundle.min.js</td>
    <td>https://getbootstrap.com/docs/4.4/getting-started/download/</td>
    <td>Use /bootstrap-4.4.1 2/dist/js/bootstrap.bundle.min.js</td>
  </tr>
  <tr>
    <td>bootstrap.min.js</td>
    <td>https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js</td>
    <td>https://getbootstrap.com/docs/4.4/getting-started/download/</td>
    <td>Use /bootstrap-4.4.1 2/dist/js/bootstrap.min.js</td>
  </tr>
  <tr>
    <td>select2.min.css</td>
    <td>https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/css/select2.min.css</td>
    <td>https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/css/select2.min.css</td>
    <td>Download this file</td>
  </tr>
  <tr>
    <td>select2.min.js</td>
    <td>https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/js/select2.min.js</td>
    <td>https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/js/select2.min.js</td>
    <td>Download this file</td>
  </tr>
  <tr>
    <td>gijgo.min.js</td>
    <td>https://cdn.jsdelivr.net/npm/gijgo@1.9.13/js/gijgo.min.js</td>
    <td>https://gijgo.com/download</td>
    <td>Use only /fonts, /css folders</td>
  </tr>
  <tr>
    <td>gijgo.min.css</td>
    <td>https://cdn.jsdelivr.net/npm/gijgo@1.9.13/css/gijgo.min.css</td>
    <td>https://gijgo.com/download</td>
    <td>Use only /fonts, /css folders</td>
  </tr>
</table>
