import uuid


def getUUID():
    # Generate a random UUID
    new_uuid = uuid.uuid4()

    # Convert the UUID to a string
    uuid_string = str(new_uuid)

    return (uuid_string)
