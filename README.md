# Parcel Patronus: Application for Managing Parcel Deliveries

This project is a Spring Web application designed to manage a parcel delivery service through automated parcel machines.  
It coordinates the operations of a small local parcel delivery company, allowing couriers and warehouse managers to efficiently handle parcel turnover through 23 automated locker machines. The application enables users to send, track, and receive parcels across 4 size categories. Additionally, it provides real-time information about locker availability by address and size.

## Technologies Used

- **Java**: Version 17  
- **Spring Framework**: Version 3.4.1  
- **Spring Data JPA**  
- **Spring Security**  
- **PostgreSQL**  
- **Thymeleaf**

## Main Functionality

The attached **diagrams.pdf** file has detailed diagrams of the application's architecture. They show the app's design, structure, and main features.

## Deployment

The application is deployed via Google Cloud, connected to a Google SQL Instance, and is currently running at:  
[https://parcelpatronus.online/]( https://parcelpatronus.online/)

To explore the functionality, you can use the following credentials for full access as an administrator:  
- **Username**: Patronus  
- **Password**: patronus  

This will give you access with the role of `ADMIN` and the position of `COO`, granting full functionality. While the application supports role differentiation (e.g., warehouse managers and couriers have limited access), you are provided with full permissions for testing purposes.

Alternatively, you can test the app as a client:  
- **Username**: client  
- **Password**: client  

<span style="color: #3c6135;">  Feel free to explore the app by clicking any buttons, entering parcel information, assigning tasks to couriers, and completing courier duties. </span>

![Login Page](resources/static/Login_demonstration.png)

## Testing

All testing has been performed manually.

## Planned Features

- Price calculation.  
- Integration with a weather management system to check if the air temperature is expected to fall below 4°C. For temperature-sensitive parcels (e.g., liquids or contact lenses), the system will redirect packages to the nearest parcel machine with cold-protected lockers.  
- Adding "return to sender" functionality and unclaimed package management.  
- Integration with SMS services to automatically inform clients about deliveries.

## Contact

Feel free to reach out to me at:  
**Email**: [marija.kotova91@gmail.com](mailto:marija.kotova91@gmail.com)  
**LinkedIn**: [www.linkedin.com/in/marija-kotova](https://www.linkedin.com/in/marija-kotova)

