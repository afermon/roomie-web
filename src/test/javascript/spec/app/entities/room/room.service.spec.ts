/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { RoomService } from 'app/entities/room/room.service';
import { IRoom, Room, RoomState, RoomType } from 'app/shared/model/room.model';

describe('Service Tests', () => {
    describe('Room Service', () => {
        let injector: TestBed;
        let service: RoomService;
        let httpMock: HttpTestingController;
        let elemDefault: IRoom;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(RoomService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Room(
                0,
                RoomState.SEARCH,
                currentDate,
                currentDate,
                'AAAAAAA',
                'AAAAAAA',
                0,
                RoomType.ROOM,
                'AAAAAAA',
                false,
                currentDate,
                false
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        created: currentDate.format(DATE_TIME_FORMAT),
                        published: currentDate.format(DATE_TIME_FORMAT),
                        availableFrom: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Room', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        created: currentDate.format(DATE_TIME_FORMAT),
                        published: currentDate.format(DATE_TIME_FORMAT),
                        availableFrom: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        created: currentDate,
                        published: currentDate,
                        availableFrom: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Room(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Room', async () => {
                const returnedFromService = Object.assign(
                    {
                        state: 'BBBBBB',
                        created: currentDate.format(DATE_TIME_FORMAT),
                        published: currentDate.format(DATE_TIME_FORMAT),
                        title: 'BBBBBB',
                        description: 'BBBBBB',
                        rooms: 1,
                        roomType: 'BBBBBB',
                        apoinmentsNotes: 'BBBBBB',
                        lookingForRoomie: true,
                        availableFrom: currentDate.format(DATE_FORMAT),
                        isPremium: true
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        created: currentDate,
                        published: currentDate,
                        availableFrom: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Room', async () => {
                const returnedFromService = Object.assign(
                    {
                        state: 'BBBBBB',
                        created: currentDate.format(DATE_TIME_FORMAT),
                        published: currentDate.format(DATE_TIME_FORMAT),
                        title: 'BBBBBB',
                        description: 'BBBBBB',
                        rooms: 1,
                        roomType: 'BBBBBB',
                        apoinmentsNotes: 'BBBBBB',
                        lookingForRoomie: true,
                        availableFrom: currentDate.format(DATE_FORMAT),
                        isPremium: true
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        created: currentDate,
                        published: currentDate,
                        availableFrom: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Room', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
