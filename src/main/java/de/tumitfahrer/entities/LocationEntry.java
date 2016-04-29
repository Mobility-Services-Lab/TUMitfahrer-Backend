/*
 * Copyright 2016 TUM Technische Universität München
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.tumitfahrer.entities;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "location_entries")
public class LocationEntry implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "location_entries_id_seq", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotEmpty
    @Column(name = "NAME", nullable = false, unique = true, length = 64)
    private String name;

    @Embedded
    private EmbeddedGeoLocation geoLocation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "locationEntry", cascade = {CascadeType.REMOVE})
    private Collection<LocationAlias> locationAliases;

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmbeddedGeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(EmbeddedGeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public Collection<LocationAlias> getLocationAliases() {
        return locationAliases;
    }
}
