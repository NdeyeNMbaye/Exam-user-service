version: '3.8'

services:

  postgres-user:
    image: postgres:15
    container_name: postgres-user
    environment:
      POSTGRES_DB: ExamUser
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: passer
    ports:
      - "5433:5432"
    networks:
      - user-network

  redis-user:
    image: redis:7-alpine
    container_name: redis-user
    ports:
      - "6379:6379"
    networks:
      - user-network

  user-service:
    image: ngoundje/user-service:latest
    container_name: user-service
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8019:8019"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres-user:5432/ExamUser
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: passer
      SPRING_DATA_REDIS_HOST: redis-user
      SPRING_DATA_REDIS_PORT: 6379
      JWT_SECRET: 404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
      JWT_EXPIRATION: 86400000
    depends_on:
      - postgres-user
      - redis-user
    networks:
      - user-network

networks:
  user-network:
    driver: bridge