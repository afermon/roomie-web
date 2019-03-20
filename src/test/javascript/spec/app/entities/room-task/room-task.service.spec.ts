/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { RoomTaskService } from 'app/entities/room-task/room-task.service';
import { IRoomTask, RoomTask, RoomTaskState } from 'app/shared/model/room-task.model';

describe('Service Tests', () => {
    describe('RoomTask Service', () => {
        let injector: TestBed;
        let service: RoomTaskService;
        let httpMock: HttpTestingController;
        let elemDefault: IRoomTask;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(RoomTaskService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new RoomTask(0, currentDate, 'AAAAAAA', 'AAAAAAA', currentDate, RoomTaskState.PENDING);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        created: currentDate.format(DATE_TIME_FORMAT),
                        deadline: currentDate.format(DATE_TIME_FORMAT)
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

            it('should create a RoomTask', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        created: currentDate.format(DATE_TIME_FORMAT),
                        deadline: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        created: currentDate,
                        deadline: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new RoomTask(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a RoomTask', async () => {
                const returnedFromService = Object.assign(
                    {
                        created: currentDate.format(DATE_TIME_FORMAT),
                        title: 'BBBBBB',
                        description: 'BBBBBB',
                        deadline: currentDate.format(DATE_TIME_FORMAT),
                        state: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        created: currentDate,
                        deadline: currentDate
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

            it('should return a list of RoomTask', async () => {
                const returnedFromService = Object.assign(
                    {
                        created: currentDate.format(DATE_TIME_FORMAT),
                        title: 'BBBBBB',
                        description: 'BBBBBB',
                        deadline: currentDate.format(DATE_TIME_FORMAT),
                        state: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        created: currentDate,
                        deadline: currentDate
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

            it('should delete a RoomTask', async () => {
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
