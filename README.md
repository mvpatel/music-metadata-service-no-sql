# Music Metadata Service

Welcome to the Music Metadata Service project! This service helps you manage information about artists and their tracks
easily.

## Features

- Add Artists: Easily add new artists with their names and aliases.
- Update Artist Names: Update the names of existing artists.
- Find Artists: Search for artists by their IDs.
- Get Artist of the Day: Find out who the artist of the day is!
- Error Handling: Comprehensive error handling for smoother user experience.

## Getting Started

To start using the Music Metadata Service, follow these simple steps:

1. Clone the Repository: Clone this repository to your local machine.
2. Install Dependencies: Navigate to the project directory and install the required dependencies using Maven.

   mvn install

3. Run the Application: Start the application locally using Maven.

   mvn spring-boot:run

4. Run with Docker: Alternatively, you can run the application using Docker.

   docker build -t music-metadata-service .
   docker run -p 8080:8080 music-metadata-service

Once the application is running, you can explore the available endpoints using tools like Postman or by visiting the
provided Swagger UI.

## Import Postman Collection

To import the Postman collection and start testing the Music Metadata Service endpoints, follow these steps:

1. Download
   the [Music Metadata Service Collection.postman_collection.json](music-metadata-service/Docs/PostManCollections/Music%20MetaData%20Service%20Collection.postman_collection.json)
   file.
2. Open Postman.
3. Click on the Import button in the top-left corner.
4. Select the downloaded JSON file.
5. The collection will be imported into Postman, and you can start testing the endpoints immediately.

## Documentation

For detailed documentation about the Music Metadata Service, refer to the (/music-metadata-service/docs/) in this
repository. You'll find guides, API references, and more to help you make the most out of this service.

## Support

If you encounter any issues or have any questions, feel free to contact me.
Mayank Patel
07960608181
bestmvpatel@gmail.com
