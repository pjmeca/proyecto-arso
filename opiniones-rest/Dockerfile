FROM mcr.microsoft.com/dotnet/sdk:6.0
WORKDIR /opiniones-rest

COPY . .
# RUN dotnet dev-certs https
RUN dotnet build

ENV ASPNETCORE_KESTREL__ENDPOINTS__HTTP__URL=http://+:5041

ENV URI_MONGODB=

CMD ["dotnet", "run"]
EXPOSE 5041

