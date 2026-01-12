# JJWT Migration Guide  
## Migrating from JJWT 0.11.5 to JJWT 0.13.0 (Spring Boot 4)

This document explains the JWT related changes made to support **Spring Boot 4** by upgrading **JJWT from version 0.11.5 to 0.13.0**.

The goal of this guide is to clearly show **what changed, why it changed, and where to look**, without altering the overall JWT design or concepts used in the course.

---

## Overview

- **Previous version:** JJWT 0.11.5  
- **Updated version:** JJWT 0.13.0  
- **Reason for change:** JJWT 0.13.0 is compatible with Spring Boot 4 and uses APIs that replace methods deprecated starting in JJWT 0.12.0  
- **Scope:** Limited to a single class for our course projects  
- **Design impact:** None  

---

## Scope of Changes

- Changes are limited to **one class only**:
  - [JwtServiceImpl.java](https://github.com/darbyluv2code/spring-boot-rest-apis/blob/main/section-06-spring-boot-rest-project-4/04-todos/src/main/java/com/luv2code/springboot/todos/service/JwtServiceImpl.java)
- All other application code remains unchanged
- JWT concepts, token structure, and validation logic remain the same
- This is an **API level migration**, not an architectural or behavioral change

---

## Why This Migration Is Required

Beginning with **JJWT 0.12.0**, several APIs commonly used in earlier versions were **deprecated** in favor of clearer and more explicit alternatives.

These include:

- `Jwts.parserBuilder()`
- `JwtParserBuilder#setSigningKey(...)`
- `JwtParser#parseClaimsJws(...)`
- Builder methods prefixed with `set` such as `setClaims`, `setSubject`
- Use of the `SignatureAlgorithm` enum when signing tokens

JJWT 0.13.0 removes reliance on these deprecated APIs and standardizes on the newer, fluent API style.

To ensure the project uses **current, supported APIs** and remains compatible with **Spring Boot 4**, the JWT implementation was updated accordingly.

---

## High Level API Changes

### Token Parsing

**Before (JJWT 0.11.5)**  
- `parserBuilder()`
- `setSigningKey(...)`
- `parseClaimsJws(...)`
- `getBody()`

**After (JJWT 0.12.0 and higher)**  
- `parser()`
- `verifyWith(...)`
- `parseSignedClaims(...)`
- `getPayload()`

---

### Token Generation

**Before (JJWT 0.11.5)**  
- `setClaims(...)`
- `setSubject(...)`
- `setIssuedAt(...)`
- `setExpiration(...)`
- `signWith(key, SignatureAlgorithm.HS256)`

**After (JJWT 0.12.0 and higher)**  
- `claims(...)`
- `subject(...)`
- `issuedAt(...)`
- `expiration(...)`
- `signWith(key, Jwts.SIG.HS256)`

---

## Code Comparison

### Before (JJWT 0.11.5)

```java
private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
}
````

```java
.signWith(getSigningKey(), SignatureAlgorithm.HS256)
```

The APIs shown above were **deprecated starting in JJWT 0.12.0**.

---

### After (JJWT 0.12.0 and higher)

```java
private Claims extractAllClaims(String token) {
    return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
}
```

```java
.signWith(getSigningKey(), Jwts.SIG.HS256)
```

These are the **current, supported APIs** recommended for use with Spring Boot 4.

---

## Methods to Review Carefully

When comparing the old and new versions of `JwtServiceImpl`, pay close attention to the following methods:

* `extractAllClaims(...)`
  Updated to use the new parsing and verification APIs

* `extractClaim(...)`
  Logic is unchanged but depends on the updated parsing pipeline

* `extractUsername(...)`
  Behavior remains the same

* `extractExpiration(...)`
  Same logic, updated claim extraction path

* `isTokenExpired(...)`
  No behavioral changes

* `isTokenValid(...)`
  Validation logic is unchanged and relies on updated claim extraction

Although several of these methods appear unchanged, they **depend on the updated parsing logic**, so reviewing them together is recommended.

---

## Key Takeaways

* The relevant API deprecations begin in **JJWT 0.12.0**
* JJWT 0.13.0 adopts the new, supported APIs
* JWT fundamentals taught in the course remain fully valid
* Only one class required updates
* Application behavior is unchanged
* The update ensures compatibility with **Spring Boot 4** and supported JJWT APIs

---

## Source Code

The updated implementation is available in the GitHub repository: [JwtServiceImpl.java](https://github.com/darbyluv2code/spring-boot-rest-apis/blob/main/section-06-spring-boot-rest-project-4/04-todos/src/main/java/com/luv2code/springboot/todos/service/JwtServiceImpl.java)

