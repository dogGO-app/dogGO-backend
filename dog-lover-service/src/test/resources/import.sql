INSERT INTO public.dog_lover (user_id, age, first_name, last_name, hobby, nickname)
VALUES ('9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5' ::uuid, 40, 'Adam', 'Małysz', 'Skoki', 'adas.malysz11');

INSERT INTO public.dog_lover (user_id, age, first_name, last_name, hobby, nickname)
VALUES ('6860ad51-bf45-46ba-b8d9-84567be4b037' ::uuid, 40, 'Jacek', 'Placek', 'Spanie', 'jacol123');

INSERT INTO public.dog (id, breed, color, description, last_vaccination_date, dog_lover_user_id)
VALUES ('9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5' ::uuid, 'york', 'blue', 'hauhau', '2020-12-22', '9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5' ::uuid);

INSERT INTO public.dog (id, breed, color, description, last_vaccination_date, name, dog_lover_user_id)
VALUES ('37faef40-2825-4b04-b8b9-b2b293e8c14d' ::uuid, 'another dog', 'white', 'miau', '2020-12-10', 'azor', '9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5' ::uuid);

INSERT INTO public.dog (id, breed, color, description, last_vaccination_date, name, dog_lover_user_id)
VALUES ('c92f4f3f-4af0-411e-857f-79d579dd767b' ::uuid, 'labrador', 'biscuit', 'lovely dog', '2020-12-10', 'burek', '6860ad51-bf45-46ba-b8d9-84567be4b037' ::uuid);

INSERT INTO public.map_marker (id, creation_date, name, description, latitude, longitude)
VALUES ('c3bd0f81-4f06-4d37-a039-251d41c6204d' ::uuid, '2020-12-22 20:50:58.000000', 'Park Mostowa',
        'Nice place', 52.4, 16.9);

INSERT INTO public.map_marker (id, creation_date, name, description, latitude, longitude)
VALUES ('87e5efcc-f8b4-48d4-9986-d2997b8cfda5' ::uuid, '2020-12-22 20:50:58.000000', 'Park Konin',
        'Not so nice place', 52.22, 18.25);

INSERT INTO public.walk (id, created_at, walk_status, dog_lover_user_id, map_marker_id)
VALUES ('19d4651e-8e47-4ddc-a558-5d52e73ddaf3' ::uuid, '2020-12-22 20:50:58.000000', 'LEFT_DESTINATION',
        '9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5' ::uuid, 'c3bd0f81-4f06-4d37-a039-251d41c6204d' ::uuid);

INSERT INTO public.walk_dogs (walk_id, dogs_id)
VALUES ('19d4651e-8e47-4ddc-a558-5d52e73ddaf3' ::uuid, '9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5' ::uuid);

INSERT INTO public.walk (id, created_at, walk_status, dog_lover_user_id, map_marker_id)
VALUES ('9f483598-5000-4b12-a4f0-a7c48b3d1cb5' ::uuid, '2020-12-20 20:50:58.000000', 'LEFT_DESTINATION',
        '9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5' ::uuid, '87e5efcc-f8b4-48d4-9986-d2997b8cfda5' ::uuid);

INSERT INTO public.walk_dogs (walk_id, dogs_id)
VALUES ('9f483598-5000-4b12-a4f0-a7c48b3d1cb5' ::uuid, '37faef40-2825-4b04-b8b9-b2b293e8c14d' ::uuid);

INSERT INTO public.walk (id, created_at, walk_status, dog_lover_user_id, map_marker_id)
VALUES ('0f68dc58-dd3b-4cb8-91a9-1c891e652d65' ::uuid, '2020-12-20 18:50:58.000000', 'ARRIVED_AT_DESTINATION',
        '6860ad51-bf45-46ba-b8d9-84567be4b037' ::uuid, '87e5efcc-f8b4-48d4-9986-d2997b8cfda5' ::uuid);

INSERT INTO public.walk_dogs (walk_id, dogs_id)
VALUES ('0f68dc58-dd3b-4cb8-91a9-1c891e652d65' ::uuid, 'c92f4f3f-4af0-411e-857f-79d579dd767b' ::uuid);

INSERT INTO public.walk (id, created_at, walk_status, dog_lover_user_id, map_marker_id)
VALUES ('c9db3a67-56db-4a2d-b926-9fdc6fb5ee65' ::uuid, '2020-12-18 20:50:58.000000', 'ARRIVED_AT_DESTINATION',
        '6860ad51-bf45-46ba-b8d9-84567be4b037' ::uuid, 'c3bd0f81-4f06-4d37-a039-251d41c6204d' ::uuid);

INSERT INTO public.walk_dogs (walk_id, dogs_id)
VALUES ('c9db3a67-56db-4a2d-b926-9fdc6fb5ee65' ::uuid, 'c92f4f3f-4af0-411e-857f-79d579dd767b' ::uuid);

INSERT INTO public.walk (id, created_at, walk_status, dog_lover_user_id, map_marker_id)
VALUES ('2f009fed-7a43-4bd2-9e3f-623ef688e953' ::uuid, '2020-12-20 16:50:58.000000', 'ONGOING',
        '9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5' ::uuid, '87e5efcc-f8b4-48d4-9986-d2997b8cfda5' ::uuid);

INSERT INTO public.walk_dogs (walk_id, dogs_id)
VALUES ('2f009fed-7a43-4bd2-9e3f-623ef688e953' ::uuid, '37faef40-2825-4b04-b8b9-b2b293e8c14d' ::uuid);

INSERT INTO public.dog_lover_like (created_at, receiver_dog_lover_walk_id, giver_dog_lover_walk_id)
VALUES ('2020-12-20 16:50:58.000000', '0f68dc58-dd3b-4cb8-91a9-1c891e652d65' ::uuid,
        '2f009fed-7a43-4bd2-9e3f-623ef688e953' ::uuid);

INSERT INTO public.dog_lover_relationship (receiver_dog_lover_user_id, giver_dog_lover_user_id, relationship_status)
VALUES ('9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5' ::uuid,
        '6860ad51-bf45-46ba-b8d9-84567be4b037' ::uuid, 'BLOCKS');

INSERT INTO public.dog_lover_relationship (receiver_dog_lover_user_id, giver_dog_lover_user_id, relationship_status)
VALUES ('6860ad51-bf45-46ba-b8d9-84567be4b037' ::uuid, '9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5' ::uuid,
        'FOLLOWS');

INSERT INTO public.user_calendar (id, date_time, description, dog_id, dog_lover_user_id)
VALUES ('50a7eaf8-3e9f-4007-b875-f1ec67ef15ce' ::uuid, '2020-12-31 18:50:58.000000', 'some description',
        '9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5' ::uuid, '9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5' ::uuid);