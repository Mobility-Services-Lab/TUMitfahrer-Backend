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

package de.tumitfahrer.services;

import de.tumitfahrer.daos.ILocationAliasDao;
import de.tumitfahrer.daos.ILocationEntryDao;
import de.tumitfahrer.entities.LocationAlias;
import de.tumitfahrer.entities.LocationEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class LocationAliasService extends AbstractService {

    @Autowired
    private ILocationEntryDao locationEntryDao;

    @Autowired
    private ILocationAliasDao locationAliasDao;

    /**
     * Retrieves a list of all location entries with corresponding aliases
     *
     * @return List of location entries
     */
    public Collection<LocationEntry> getAllEntries() {
        return locationEntryDao.getAllEntriesWithAliases();
    }

    /**
     * Searches for matching aliases based on the given search term
     *
     * @param query Search term
     * @return List of location aliases
     */
    public Collection<LocationAlias> searchForQuery(final String query) {
        return locationAliasDao.findByPartialName(query);
    }
}
