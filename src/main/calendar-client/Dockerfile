# Build the angular application
FROM node:14.17.0-alpine as build-step
RUN mkdir -p /app
WORKDIR /app
COPY package.json /app
# COPY package-lock.json /app
# COPY angular.json /app
# COPY tsconfig.json /app
# COPY tsconfig.app.json /app
COPY . /app
RUN npm install

# Build the application
# RUN npm run build

#use the compiled app, ready for production with Nginx
FROM nginx:1.20.1

COPY --from=build-step /app/dist/calendar-client /usr/share/nginx/html

# Expose port 80 to the Docker host, so we can access it
# from the outside.
EXPOSE 4200:80



