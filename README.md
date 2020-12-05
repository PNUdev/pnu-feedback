## Running DB Instructions
 * create and run db container locally
 ```
 docker run --name pnu_feedback_db \
                 -e POSTGRES_PASSWORD=root \
                 -e POSTGRES_USERNAME=postgres \
                 -e POSTGRES_DB=pnu_feedback \
                 -p 5435:5432 -d postgres
 ```
