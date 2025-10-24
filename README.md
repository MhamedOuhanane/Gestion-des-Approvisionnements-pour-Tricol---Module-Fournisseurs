# Tricol - Gestion des Fournisseurs

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-blue.svg)](https://jakarta.ee/)
[![Maven](https://img.shields.io/badge/Maven-3.9.11-red.svg)](https://maven.apache.org/)
[![Hibernate](https://img.shields.io/badge/Hibernate-6.4-green.svg)](https://hibernate.org/)
[![Spring](https://img.shields.io/badge/Spring-6.1.4-brightgreen.svg)](https://spring.io/projects/spring-framework)
[![Tomcat](https://img.shields.io/badge/Tomcat-11.0.11-yellow.svg)](https://tomcat.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Jackson](https://img.shields.io/badge/Jackson-2.17-blue.svg)](https://github.com/FasterXML/jackson)

---

## Contexte du projet

Tricol est une entreprise spécialisée dans la conception et la fabrication de vêtements professionnels. Les dirigeants souhaitent mettre en place une application pour **gérer efficacement les approvisionnements** de l’entreprise.

La première version de l'application se concentre sur **la gestion des fournisseurs**, en posant les bases pour un futur système complet incluant la gestion des produits, commandes et stocks.

---

## Objectif du projet

Développer un module robuste de **gestion des fournisseurs** en utilisant les fondamentaux de Spring Core, avec une architecture extensible pour les futures évolutions.

---

## Fonctionnalités principales

- **Ajouter un fournisseur** : Enregistrer un fournisseur avec les informations suivantes :
    - Société
    - Adresse
    - Contact
    - Email
    - Téléphone
    - Ville
    - ICE (Identifiant Commun Entreprise)

- **Modifier un fournisseur** : Mettre à jour les informations d’un fournisseur existant.
- **Supprimer un fournisseur** : Retirer un fournisseur du système.
- **Consulter la liste des fournisseurs** : Afficher tous les fournisseurs avec tri possible par nom.

---

## Architecture et technologies

### Technologies principales
- **Java 17**
- **Spring 6 (Spring Core, Spring MVC, Spring Data JPA)**
- **Jakarta EE 10**
- **Hibernate 6.2**
- **Tomcat 11.0.11**
- **Maven 3.9.11**

### Architecture
- **Conception en couches** :
    - **Controller** : Gestion des requêtes HTTP et des réponses JSON
    - **Service** : Logique métier et validation
    - **Repository** : Accès aux données via Spring Data JPA

- **Principes Spring utilisés** :
    - Conteneur IoC pour la gestion des dépendances
    - Spring Beans et scopes
    - ApplicationContext et BeanFactory
    - Configuration Spring : XML, annotations
    - Component Scanning
    - Patterns Service et Controller

---

## API REST - Endpoints

| Méthode | Endpoint                  | Description                       |
|---------|---------------------------|-----------------------------------|
| GET     | `/fournisseurs`           | Lister tous les fournisseurs      |
| GET     | `/fournisseurs/{id}`      | Obtenir un fournisseur par ID     |
| POST    | `/fournisseurs`           | Ajouter un nouveau fournisseur    |
| PUT     | `/fournisseurs/{id}`      | Mettre à jour un fournisseur      |
| DELETE  | `/fournisseurs/{id}`      | Supprimer un fournisseur          |

**Exemple JSON pour POST/PUT :**
```json
{
    "supplier": {
        "uuid": "b6e88fe8-02c4-4d16-b2f9-962cc9adc378",
        "company": "watsapp",
        "address": "Mekness",
        "contact": "fournissour3",
        "email": "fournissour3@gmial.com",
        "phone": "+212687285943",
        "city": "Mekness",
        "ice": 988989397549578
    },
    "message": "Le fournisseur de mail 'fournissour3@gmial.com' a étre ajouté avec succès !"
}

```
**Exemple JSON pour GET :**
```json
{
    "suppliers": [
        {
            "uuid": "98cee8df-f4e8-42ae-90b1-f1ca7c7a68ac",
            "company": "instagrame",
            "address": "Fes",
            "contact": "fournissour2",
            "email": "fournissour2@gmial.com",
            "phone": "+212687285943",
            "city": "Fes",
            "ice": 98898947549578
        },
        {
            "uuid": "b7c486e1-1a34-461d-aa93-4a4bea389a69",
            "company": "facebook",
            "address": "Rabat",
            "contact": "fournissour1",
            "email": "fournissour1@gmial.com",
            "phone": "+212687785943",
            "city": "Maroc.Fes",
            "ice": 98898947849578
        },
        {
            "uuid": "b6e88fe8-02c4-4d16-b2f9-962cc9adc378",
            "company": "watsapp",
            "address": "Mekness",
            "contact": "fournissour3",
            "email": "fournissour3@gmial.com",
            "phone": "+212687285943",
            "city": "Mekness",
            "ice": 988989397549578
        }
    ],
    "message": "Trouver tous les fournisseur exist avec succès!"
}
```
