FROM harbor.shopeemobile.com/shopee/credit/maven:3.5.3-jdk-8-alpine as builder

ARG role

ENV BIZ_NAME "$DOCKER_GROUP-$APP_NAME"
WORKDIR /$BIZ_NAME
COPY . .

ENV http_proxy http://10.129.97.140:3128
ENV https_proxy http://10.129.97.140:3128
RUN mvn clean package
RUN mkdir target && mv seamiter-admin/target/*.jar target/ 
RUN if [[ ${role} == "main" ]] || [[ ${role} == "nonlive" ]]; then cp seamiter-admin/src/main/resources/application-$REGION-$ENV.properties target/application-$REGION-$ENV.properties;else cp seamiter-admin/src/main/resources/application-$REGION-$ENV-dr.properties target/application-$REGION-$ENV.properties;fi

FROM harbor.shopeemobile.com/shopee/credit/credit_apollo:0.2

RUN ln -fs /usr/share/zoneinfo/$TZ /etc/localtime \
    && apt update && apt install -y tzdata \
    && dpkg-reconfigure -f noninteractive tzdata

ENV BIZ_NAME "$DOCKER_GROUP-$APP_NAME"
ENV ENV $ENV
ENV REGION $REGION

WORKDIR /$BIZ_NAME

COPY --from=builder /$BIZ_NAME/target/ .
COPY ./scripts ./scripts

RUN chmod +x ./scripts/*

ENTRYPOINT [ "./scripts/entrypoint.sh" ]
