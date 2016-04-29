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
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "location_aliases")
public class LocationAlias implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "location_aliases_id_seq", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotEmpty
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "LOCATION_ENTRY_ID")
    private LocationEntry locationEntry;

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

    public LocationEntry getLocationEntry() {
        return locationEntry;
    }

    public void setLocationEntry(LocationEntry locationEntry) {
        this.locationEntry = locationEntry;
    }
}
