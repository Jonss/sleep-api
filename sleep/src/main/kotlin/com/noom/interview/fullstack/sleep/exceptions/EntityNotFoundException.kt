package com.noom.interview.fullstack.sleep.exceptions

class EntityNotFoundException(
    entityName: String,
) : RuntimeException("$entityName not found")
