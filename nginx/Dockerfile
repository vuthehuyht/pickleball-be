FROM nginx:latest

COPY nginx.conf /etc/nginx/sites-available/datsan
RUN ln -s /etc/nginx/sites-available/datsan /etc/nginx/sites-enabled

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
