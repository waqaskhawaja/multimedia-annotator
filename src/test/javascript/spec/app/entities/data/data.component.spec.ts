/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { DataComponent } from 'app/entities/data/data.component';
import { DataService } from 'app/entities/data/data.service';
import { Data } from 'app/shared/model/data.model';

describe('Component Tests', () => {
    describe('Data Management Component', () => {
        let comp: DataComponent;
        let fixture: ComponentFixture<DataComponent>;
        let service: DataService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [DataComponent],
                providers: []
            })
                .overrideTemplate(DataComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DataComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DataService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Data(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.data[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
